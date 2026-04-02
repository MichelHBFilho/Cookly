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
            
            self.posts = posts.map { postResponse in postResponse.toPost() }
                
        } catch {
            ErrorManager.shared.handle(error)
        }
    }
}
