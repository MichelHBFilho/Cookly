//
//  MyButton.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct MyButton: View {
    let title: String
    let color : Color
    let action : () -> Void
    
    var body: some View {
        
        Button(title) {
            action()
        }
        .tint(color)
        .font(.title3)
        .fontWeight(.semibold)
        .controlSize(.large)
        .buttonSizing(.flexible)
        .frame(width: 150)
        .buttonStyle(.glassProminent)
        .padding()
        
    }
}
