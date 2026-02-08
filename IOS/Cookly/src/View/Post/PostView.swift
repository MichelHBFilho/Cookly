//
//  SummarizedPostView.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct PostView: View {
    @StateObject var viewModel: PostViewModel
    @State private var showingDetails: Bool = false
    @State private var showingComments: Bool = false
    var body: some View {
        VStack() {
            
            if let profile = viewModel.profile {
                SummarizedProfileView(profile: profile)
                    .offset(y: 30)
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
                        .frame(width: 30)
                        .foregroundStyle(.cooklyRed)
                }
                
                Button {
                    showingComments.toggle()
                } label: {
                    Image(systemName: "message")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 30)
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
        }
        .popover(isPresented: $showingDetails, content: { recipeDetails })
        .popover(isPresented: $showingComments, content: { comments })
        .task {
            await viewModel.loadData()
            await viewModel.updateComments()
        }
    }
    
    var recipeDetails: some View {
        VStack {
            Text("Prepare time: \(viewModel.post.recipe.prepareTime)")
                .font(.title2)
                .fontWeight(.medium)
                .foregroundStyle(.cooklyGreen)
                .padding()
            
            Text("Steps:")
                .font(.title)
                .fontWeight(.semibold)
            ForEach(viewModel.post.recipe.stepByStep.indices, id: \.self) { index in
                HStack(alignment: .top, spacing: 8) {
                    
                    Text("\(index + 1)")
                        .font(.title3)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .frame(width: 22, height: 22)
                        .background(Circle().fill(.cooklyBlue))
                    
                    Text(viewModel.post.recipe.stepByStep[index])
                        .font(.title3)
                        .foregroundStyle(.cooklyGray)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .padding(.vertical, 4)
            }
            
            Spacer()
        }.padding()
    }
    
    var comments: some View {
        VStack() {
            Text("Comments")
                .font(.largeTitle)
                .fontWeight(.semibold)
                .foregroundStyle(.cooklyGray)
                .padding(.top)
            
            ScrollView {
                ForEach(viewModel.post.comments, id: \.id) { comment in
                    ReadCommentView(comment: comment)
                }
            }
            
            NewCommentView(content: $viewModel.newCommentText, sendComment: viewModel.sendComment)
        }
    }
}

#Preview {
    let post = Post(
        id: "40479f2b-37f7-495b-a4e1-9f5eab9ba6b9",
        recipe: Recipe(
            name: "Cheese bread",
            prepareTime: 25,
            stepByStep: [
                "Turn on the oven",
                "Turn off the oven"
            ]
        ),
        comments: [
        ],
        likes: 12,
        author: "marischultz",
        description: "A bread with cheese inside.",
        createdAt: Date(),
        imagePaths: ["437fded8-b4f7-466c-8d02-5cdc9e0f51eb_istockphoto-1024883060-612x612.jpg",
                     "d6370711-82a5-4e60-b58a-2d73adbbf93f_8b963ddc5e21b66ba2022667c10e9bf6.jpg"]
    )
    
    Task { await setupAuth() }
    
    return PostView(viewModel:  PostViewModel(post: post))
}
