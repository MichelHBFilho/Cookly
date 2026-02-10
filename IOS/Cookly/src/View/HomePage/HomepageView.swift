//
//  HomepageView.swift
//  Cookly
//
//  Created by Michel Filho on 08/02/26.
//

import SwiftUI

struct HomepageView: View {
    @StateObject private var viewModel = HomepageViewModel()
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                MenuView()
                    .padding()
                Divider()
                    .foregroundStyle(.cooklyBlue)

                ForEach(viewModel.posts) { post in
                    PostView(viewModel: PostViewModel(post: post))
                    Divider()
                }
            }
        }
        .task {
            await viewModel.setup()
        }
    }
}

#Preview {
    HomepageView()
}
