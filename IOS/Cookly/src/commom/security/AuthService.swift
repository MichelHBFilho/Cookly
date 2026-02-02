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
        let token: TokenResponse = try await APIService.shared.request(
            endpoint: "authentication/login",
            method: .POST,
            requiresAuth: false,
            body: user)
        
        KeychainService.shared.save(token.token, to: "accessToken")
    }
    
    public func generateRefreshToken() async throws {
        let token: TokenResponse = try await APIService.shared.request(
            endpoint: "authentication/generate-refresh",
            method: .GET,
            requiresAuth: true)
        
        KeychainService.shared.save(token.token, to: "refreshToken")
    }
    
    public func refreshToken() async throws {
        let refreshToken = try KeychainService.shared.load(from: "refreshToken")
        
        let token: TokenResponse = try await APIService.shared.request(
            endpoint: "authentication/refresh/\(refreshToken)",
            method: .GET,
            requiresAuth: false)
        
        KeychainService.shared.save(token.token, to: "accessToken")
    }
    
    
}
