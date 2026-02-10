//
//  NewCommentView.swift
//  Cookly
//
//  Created by Michel Filho on 07/02/26.
//

import SwiftUI

struct NewCommentView: View {
    @Binding var content: String
    let sendComment: () async -> Void
    var body: some View {
        HStack {
            TextField("Comment here!", text: $content)
            
            Button {
                Task { await sendComment() }
            } label: {
                Image(systemName: "paperplane.fill")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 30)
            }

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
    NewCommentView(content: .constant("")) {
        
    }
}
