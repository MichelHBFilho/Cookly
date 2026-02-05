//
//  AuthService.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation
import Combine

@MainActor
@Observable
class AuthService {
    static public var shared = AuthService()
    
    public var isUserLogged: Bool = false
    
    private init() {
        self.isUserLogged = hasRefreshToken()
    }

    private func hasRefreshToken() -> Bool {
        (try? KeychainService.shared.load(from: "refreshToken")) != nil
    }

    func loginSucceeded() {
        isUserLogged = true
    }

    func logout() {
        try? KeychainService.shared.delete(from: "refreshToken")
        isUserLogged = false
    }

    
    public func login(user : LoginRequest) async throws {
        let (token, firstStatusCode) = try await APIService.shared.request(
            endpoint: "authentication/login",
            method: .POST,
            requiresAuth: false,
            body: user) as (TokenResponse?, Int)
        
        guard let token else { throw APIError.NotFound }
        
        KeychainService.shared.save(token.token, to: "accessToken")
        
        let (refreshToken, secondStatusCode) = try await APIService.shared.request(
            endpoint: "authentication/generate-refresh",
            method: .GET,
            requiresAuth: true,
            authToken: token.token
        ) as (RefreshTokenResponse?, Int)
        
        guard let refreshToken else { throw APIError.NotFound }
        
        KeychainService.shared.save(refreshToken.token, to: "refreshToken")
        
        loginSucceeded()
    }
    
    public func generateRefreshToken() async throws {
        let (refreshToken, _) = try await APIService.shared.request(
            endpoint: "authentication/generate-refresh",
            method: .GET,
            requiresAuth: true,
        ) as (RefreshTokenResponse?, Int)
        
        guard let refreshToken else { throw APIError.NotFound }
        
        KeychainService.shared.save(refreshToken.token, to: "refreshToken")
    }
    
    public func refreshToken() async throws {
        let refreshToken = try? KeychainService.shared.load(from: "refreshToken")
        do {
            let (token, _) = try await APIService.shared.request(
                endpoint: "authentication/refresh/\(refreshToken!)",
                method: .GET,
                requiresAuth: false) as (TokenResponse?, Int)
            
            guard let token else { throw APIError.NotFound }
            
            KeychainService.shared.save(token.token, to: "accessToken")
            
            loginSucceeded()
        } catch APIError.NotFound {
            logout()
        } catch {
            throw error
        }
        
    }
    
    public func setAuthHeader(_ request : inout URLRequest) async throws {
        guard hasRefreshToken() else {
            throw APIError.Unauthenticated
        }
        
        try await refreshToken()
        
        let token = try KeychainService.shared.load(from: "accessToken")
        
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
    }
    
    
}
