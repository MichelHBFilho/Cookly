//
//  LoginView.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct LoginView: View {
    @State var viewModel : LoginViewModel = LoginViewModel()
    
    var body: some View {
        
        VStack() {
            Text("Login:")
                .font(.title)
                .fontWeight(.semibold)
                .foregroundStyle(.cooklyGray)
                .padding(.horizontal)
            
            MyTextField(value: $viewModel.loginRequest.username, fieldName: "Username")
            MyTextField(value: $viewModel.loginRequest.password, fieldName: "Password")
            
            MyButton(title: "Login", color: viewModel.color) {
                Task {
                    await viewModel.doRequest()
                }
            }
            .animation(.spring, value: viewModel.color)
            
        }
        
    }
}

#Preview {
    LoginView()
}
