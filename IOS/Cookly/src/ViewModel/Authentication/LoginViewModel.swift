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
    private var loginStatus: LoginStatus = .nothing
    var color: Color {
        switch(loginStatus) {
        case .nothing: .cooklyBlue
        case .successfull: .cooklyGreen
        case .wrongCredential: .errorRed
        }
    }
    func doRequest() async {
        do {
            try await AuthService.shared.login(user: loginRequest)
            loginStatus = .successfull
        } catch {
            loginStatus = .wrongCredential
            
            try? await Task.sleep(for: .seconds(1))
            
            loginStatus = .nothing
        }
    }
}
