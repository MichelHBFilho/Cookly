//
//  AuthService.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

class AuthService {
    static public var shared = AuthService()
    
    public var isUserLogged: Bool {
        do {
            let refreshToken = try KeychainService.shared.load(from: "refreshToken")
            return refreshToken != ""
        } catch {
            return false
        }
    }
    
    public func login(user : LoginRequest) async throws {
        let (token, firstStatusCode) = try await APIService.shared.request(
            endpoint: "authentication/login",
            method: .POST,
            requiresAuth: false,
            body: user) as (TokenResponse, Int)
        
        KeychainService.shared.save(token.token, to: "accessToken")
        
        let (refreshToken, secondStatusCode) = try await APIService.shared.request(
            endpoint: "authentication/generate-refresh",
            method: .GET,
            requiresAuth: true,
            authToken: token.token
        ) as (RefreshTokenResponse, Int)
        
        KeychainService.shared.save(refreshToken.token, to: "refreshToken")
    }
    
    public func generateRefreshToken() async throws {
        let (refreshToken, secondStatusCode) = try await APIService.shared.request(
            endpoint: "authentication/generate-refresh",
            method: .GET,
            requiresAuth: true,
        ) as (RefreshTokenResponse, Int)
        
        KeychainService.shared.save(refreshToken.token, to: "refreshToken")
    }
    
    public func refreshToken() async throws {
        let refreshToken = try KeychainService.shared.load(from: "refreshToken")
        
        do {
            let (token, statusCode) = try await APIService.shared.request(
                endpoint: "authentication/refresh/\(refreshToken)",
                method: .GET,
                requiresAuth: false) as (TokenResponse, Int)
            
            KeychainService.shared.save(token.token, to: "accessToken")
        } catch APIError.NotFound {
            try KeychainService.shared.delete(from: "refreshToken")
        } catch {
            throw error
        }
        
    }
    
    public func setAuthHeader(_ request : inout URLRequest) async throws {
        guard isUserLogged else {
            throw APIError.Unauthenticated
        }
        
        try await refreshToken()
        
        let token = try KeychainService.shared.load(from: "accessToken")
        
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
    }
    
    
}
