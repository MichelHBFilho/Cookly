//
//  RegisterView.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI
import UIKit
import PhotosUI

struct RegisterView: View {
    
    @State var viewModel = RegisterViewModel()
    @State var photosPickerItem: PhotosPickerItem?
    @State var photosPickerImage: Image? = Image("DefaultProfilePicture")
    var color: Color {
        switch(viewModel.requestStatus) {
        case .badRequest: .errorRed
        case .success: .cooklyGreen
        case .nothing: .cooklyBlue
        }
    }
    
    var body: some View {
        VStack {
            
            PhotosPicker(selection: $photosPickerItem) {
                if let photosPickerImage {
                    photosPickerImage
                        .resizable()
                        .scaledToFit()
                        .frame(width: 250)
                        .clipShape(Circle())
                }
            }
                .onChange(of: photosPickerItem) { oldValue, newValue in
                    Task {
                        do {
                            photosPickerImage = try await photosPickerItem?.loadTransferable(type: Image.self)
                            if let data = try await photosPickerItem?.loadTransferable(type: Data.self) {
                                viewModel.profilePicture = UIImage(data: data)
                            }
                        } catch {
                            
                        }
                    }
                }
            
            MyTextField(value: $viewModel.form.username, fieldName: "Username")
            MyTextField(value: $viewModel.form.password, fieldName: "Password")
            MyTextField(value: $viewModel.form.name, fieldName: "Name")
            MyTextField(value: $viewModel.form.lastName, fieldName: "Last name")
            DatePicker("Birthday", selection: $viewModel.form.birthDay, displayedComponents: .date)
            
            MyButton(title: "Register", color: color) {
                Task {
                    do {
                        try await viewModel.doRequest()
                    } catch {
                        print(error)
                    }
                }
            }
            .animation(.spring, value: color)
            
        }
    }
}

#Preview {
    RegisterView()
}
