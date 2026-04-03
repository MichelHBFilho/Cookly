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
            }
            
            TabView {
                ForEach(viewModel.post.imagePaths, id: \.self) { imageUrl in
                    AsyncImage(url: URL(string: "\(APIService.shared.baseURL)images/post/\(imageUrl)")!) { image in
                        image
                            .resizable()
                            .scaledToFit()
                            .clipped()
                    } placeholder: {
                    }
                }
            }
            .tabViewStyle(.page)
            .indexViewStyle(.page(backgroundDisplayMode: .always))
            .frame(maxWidth: .infinity, idealHeight: 330)
            
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
        .frame(idealHeight: 460, maxHeight: 600)
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
    let hmpv = HomepageViewModel()
    Task { await hmpv.setup() }
    let post = hmpv.posts.first!
    
    Task { await setupAuth() }
    
    return PostView(viewModel:  PostViewModel(post: post))
}
