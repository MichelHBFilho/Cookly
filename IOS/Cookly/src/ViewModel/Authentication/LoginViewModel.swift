//
//  LoginViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation
import SwiftUI

@Observable
class LoginViewModel {
    
    enum LoginStatus  {
        case successfull
        case wrongCredential
        case nothing
    }
    
    init() {}
    
    var loginRequest = LoginRequest()
    var loginStatus: LoginStatus = .nothing
    
    func doRequest() async {
        do {
            try await AuthService.shared.login(user: loginRequest)
            loginStatus = .successfull
            Router.shared.route = .homepage
        } catch {
            loginStatus = .wrongCredential
            
            try? await Task.sleep(for: .seconds(1))
            
            loginStatus = .nothing
        }
    }
}
