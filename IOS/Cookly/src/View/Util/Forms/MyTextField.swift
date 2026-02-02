//
//  MyTextField.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct MyTextField: View {
    @Binding var value: String
    let fieldName: String
    var body: some View {
        HStack {
            Text(fieldName + ":")
                .fontWeight(.semibold)
                .foregroundStyle(.cooklyBlue)
            
            TextField(fieldName, text: $value)
        }
        .padding()
        .overlay {
            RoundedRectangle(cornerRadius: 15)
                .stroke(lineWidth: 2)
                .foregroundStyle(.cooklyLightBlue)
                .shadow(radius: 5)
        }
        .padding()
    }
}

#Preview {
    MyTextField(value: Binding.constant("Texto"), fieldName: "Username")
}
