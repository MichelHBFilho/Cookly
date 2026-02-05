//
//  SummarizedPostView.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct PostView: View {
    @State var viewModel: PostViewModel
    var body: some View {
        VStack {
            
            if let profile = viewModel.profile {
                SummarizedProfileView(profile: profile)
            } else {
                
            }
            
            TabView {
                ForEach(viewModel.post.imagePaths, id: \.self) { imageUrl in
                    AsyncImage(url: URL(string: "\(APIService.shared.baseURL)images/post/\(imageUrl)")!) { image in
                        image
                            .resizable()
                            .scaledToFit()
                            .frame(maxHeight: 300)
                    } placeholder: {
                    }
                }
            }
            .tabViewStyle(.page)
            .indexViewStyle(.page(backgroundDisplayMode: .always))
            .frame(height: 355)
            
            HStack {
                Button {
                    Task { await viewModel.toggleLike() }
                } label: {
                    Image(systemName: (viewModel.isPostLikedByUser ? "heart.fill" : "heart"))
                        .resizable()
                        .scaledToFit()
                        .frame(width: 20)
                        .foregroundStyle(.cooklyRed)
                }
                
                Button {
                    
                } label: {
                    Image(systemName: "message")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 20)
                        .foregroundStyle(.cooklyGray)
                }
            }
            
            
        }
        .task {
            await viewModel.loadData()
        }
    }
}

#Preview {
    let post = Post(
        id: "2c723ebe-4d82-4c58-910e-71d6f4fd8465",
        recipe: Recipe(
            name: "Cheese bread",
            prepareTime: 25,
            stepByStep: [
                "Turn on the oven",
                "Turn off the oven"
            ]
        ),
        comments: [
            Comment(
                id: "05d86b4f-0d4c-43fd-9391-927a3a4d82ce",
                author: "michelhbfilho",
                content: "Very good!",
                createdAt: Date()
            ),
            Comment(
                id: "v05d86b4f-0d4c-43fd-9391-927a3a4d82ce",
                author: "marishcultz",
                content: "I don't liked it.",
                createdAt: Date()
            )
        ],
        likes: 12,
        author: "marischultz",
        description: "A bread with cheese inside.",
        createdAt: Date(),
        imagePaths: ["b9538fa3-274b-4ef4-968f-349c0cde4a1d_8b963ddc5e21b66ba2022667c10e9bf6.jpg", "6b3e80c2-e86d-4f24-b3c9-7cf1e66025b8_istockphoto-1024883060-612x612.jpg"]
    )
    
    Task { await setupAuth() }
    
    return PostView(viewModel:  PostViewModel(post: post))
}
