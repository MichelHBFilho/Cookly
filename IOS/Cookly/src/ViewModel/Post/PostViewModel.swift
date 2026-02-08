//
//  SummarizedPostViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation
import Combine
class PostViewModel: ObservableObject {
    @Published var post: Post
    @Published var profile: Profile?
    @Published var isPostLikedByUser = false
    @Published var formattedCreatedAt = ""
    @Published var newCommentText = ""
    
    init(post: Post) {
        self.post = post
        self.profile = nil
    }
    
    func loadData() async {
        profile = await APICommomCalls.shared.getProfileByUsername(post.author)
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        formattedCreatedAt = dateFormatter.string(from: post.createdAt)
    }
    
    func toggleLike() async {
        do {
            if isPostLikedByUser {
                let (_, _) = try await APIService.shared.request(
                    endpoint: "post/\(post.id)/like",
                    method: .DELETE,
                    requiresAuth: true
                ) as (EmptyResponse?, Int)
                
                isPostLikedByUser = false;
            } else {
                let (_, _) = try await APIService.shared.request(
                    endpoint: "post/\(post.id)/like",
                    method: .POST,
                    requiresAuth: true
                ) as (EmptyResponse?, Int)
                
                isPostLikedByUser = true
            }
        } catch {
            print(error)
            ErrorManager.shared.handle(error)
        }
    }
    
    func sendComment() async {
        do {
            let (_, _) = try await APIService.shared.request(
                endpoint: "post/\(post.id)/comment",
                method: .POST,
                requiresAuth: true,
                body: newCommentText
            ) as (EmptyResponse?, Int)
            
            await updateComments()
        } catch {
            
        }
    }
    
    func updateComments() async {
        do {
            let (post, _) = try await APIService.shared.request(
                endpoint: "post/id/\(post.id)",
                method: .GET,
                requiresAuth: true
            ) as (PostResponse?, Int)
            
            guard let post else { throw APIError.NotFound }
            
            let newComments = post.comments.map(
                { commentResponse in
                    let dateFormatter = ISO8601DateFormatter()
                    return Comment(
                        id: commentResponse.id,
                        author: commentResponse.author,
                        content: commentResponse.text,
                        createdAt: dateFormatter.date(from: commentResponse.createdAt) ?? Date()
                    )
            })
            
            self.post.comments.removeAll()
            self.post.comments.append(contentsOf: newComments)
            
            newCommentText = ""
        } catch {
            print(error)
            ErrorManager.shared.handle(error)
        }
    }
}
