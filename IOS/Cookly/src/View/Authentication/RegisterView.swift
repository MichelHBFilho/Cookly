//
//  RegisterView.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct RegisterView: View {
    
    @State var viewModel = RegisterViewModel()
    
    var body: some View {
        VStack {
            
            MyTextField(value: $viewModel.form.username, fieldName: "Username")
            MyTextField(value: $viewModel.form.password, fieldName: "Password")
            MyTextField(value: $viewModel.form.name, fieldName: "Name")
            MyTextField(value: $viewModel.form.lastName, fieldName: "Last name")
            DatePicker("Birthday", selection: $viewModel.form.birthDay, displayedComponents: .date)
            
            MyButton(title: "Register", color: .cooklyBlue) {
                
            }
            
        }
    }
}

#Preview {
    RegisterView()
}
