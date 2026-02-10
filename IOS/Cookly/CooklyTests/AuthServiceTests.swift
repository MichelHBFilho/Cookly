//
//  AuthServiceTests.swift
//  Cookly
//
//  Created by Michel Filho on 03/02/26.
//

import Foundation
import Testing
@testable import Cookly

struct AuthServiceTests {
    
    @Test func shouldGetAValidRefreshToken() async throws {
        
        try await AuthService.shared.login(user: LoginRequest(username: "TestUser", password: "TestPassword"))
        
        let token = try await KeychainService.shared.load(from: "accessToken")
        print(token)
        let refreshToken = try await KeychainService.shared.load(from: "refreshToken")
        
        print(refreshToken)
        
    }
    
}
