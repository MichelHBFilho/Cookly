//
//  SummarizedPostViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

@Observable
class PostViewModel {
    let post: Post
    var profile: Profile?
    var isPostLikedByUser = false
    var formattedCreatedAt = ""
    
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
}
