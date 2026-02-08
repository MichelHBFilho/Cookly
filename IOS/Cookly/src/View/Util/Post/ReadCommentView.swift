//
//  ReadCommentView.swift
//  Cookly
//
//  Created by Michel Filho on 07/02/26.
//

import SwiftUI

struct ReadCommentView : View {
    let comment: Comment
    @State private var profile: Profile? = nil
    var body: some View {
        VStack {
            if let profile {
                SummarizedProfileView(profile: profile)
            }
            
            Text(comment.content)
                .font(.title3)
                .padding(.horizontal)
        }
        .task(id: comment.id) {
            profile = nil
            profile = await APICommomCalls.shared.getProfileByUsername(comment.author)
        }
    }
}

#Preview {
    ReadCommentView(
        comment: Comment(
            id: "24142",
            author: "michelhbfilho",
            content: "Muito top, super recomendo, da proxima vez farei uma porção maior.",
            createdAt: Date()
        )
    )
}
