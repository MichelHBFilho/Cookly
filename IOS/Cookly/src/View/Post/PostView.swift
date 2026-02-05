//
//  SummarizedPostView.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct PostView: View {
    @State var viewModel: PostViewModel
    @State private var showingDetails: Bool = false
    var body: some View {
        VStack() {
            
            if let profile = viewModel.profile {
                SummarizedProfileView(profile: profile)
                    .offset(y: 30)
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
            .frame(maxHeight: 330)
            
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
            }.padding(.horizontal)
            
            HStack {
                Text(viewModel.post.recipe.name)
                    .font(.title2)
                    .fontWeight(.semibold)
                
                Spacer()
                
                Text(viewModel.formattedCreatedAt)
            }.padding(.horizontal)
            
            HStack {
                ExpandableText(text: viewModel.post.description)
                    .font(.headline)
                    .foregroundStyle(.cooklyGray)
                Spacer()
            }.padding(.horizontal)
            
            MyButton(
                title: showingDetails ? "See less" : "See more",
                color: .cooklyBlue) {
                    showingDetails.toggle()
            }
            
            if showingDetails {
                Text("Prepare time: \(viewModel.post.recipe.prepareTime)")
                    .fontWeight(.medium)
                    .foregroundStyle(.cooklyGreen)
                Text("Steps:")
                    .font(.title3)
                    .fontWeight(.semibold)
                ForEach(viewModel.post.recipe.stepByStep.indices, id: \.self) { index in
                    HStack(alignment: .top, spacing: 8) {

                            Text("\(index + 1)")
                                .font(.caption)
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                                .frame(width: 22, height: 22)
                                .background(Circle().fill(.cooklyBlue))

                            Text(viewModel.post.recipe.stepByStep[index])
                                .font(.body)
                                .foregroundStyle(.cooklyGray)
                                .fixedSize(horizontal: false, vertical: true)
                    }
                        .padding(.vertical, 4)
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
        id: "3ddcdec4-9d66-4b47-a98f-262abc7d5001",
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
        imagePaths: ["1f2f2a5c-2feb-458d-951d-a9a0e514f672_istockphoto-1024883060-612x612.jpg",
            "a4a78ef3-0bd5-4530-91c2-bd545762d249_8b963ddc5e21b66ba2022667c10e9bf6.jpg"]
    )
    
    Task { await setupAuth() }
    
    return PostView(viewModel:  PostViewModel(post: post))
}
