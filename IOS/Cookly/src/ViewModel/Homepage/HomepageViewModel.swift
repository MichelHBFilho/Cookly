//
//  HomepageViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 08/02/26.
//

import Foundation
import Combine

class HomepageViewModel : ObservableObject {
    @Published var posts: [Post] = []
    
    func setup() async {
        do {
            let (posts, _) = try await APIService.shared.request(
                endpoint: "post",
                method: .GET,
                requiresAuth: true
            ) as ([PostResponse]?, Int)
            
            guard let posts else { throw APIError.ServerError }
            
            let dateFormatter = ISO8601DateFormatter()
            
            self.posts = posts.map { postResponse in
                return Post(
                    id: postResponse.id,
                    recipe: Recipe(
                        name: postResponse.recipe.name,
                        prepareTime: postResponse.recipe.prepareTime,
                        stepByStep: postResponse.recipe.stepByStep
                    ),
                    comments: postResponse.comments.map { comment in
                        return Comment(
                            id: comment.id,
                            author: comment.author,
                            content: comment.text,
                            createdAt: dateFormatter.date(from: comment.createdAt) ?? Date()
                        )
                    },
                    likes: postResponse.likes,
                    author: postResponse.author,
                    description: postResponse.description,
                    createdAt: dateFormatter.date(from: postResponse.createdAt) ?? Date(),
                    imagePaths: postResponse.imagesPaths
                )
            }
        } catch {
            ErrorManager.shared.handle(error)
        }
    }
}
