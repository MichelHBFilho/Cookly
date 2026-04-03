//
//  SummarizedProfileView.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct SummarizedProfileView : View {
    
    let profile: Profile
    @State var isFollowed: Bool = false
    
    var body: some View {
        HStack {
            AsyncImage(url: URL(string: "\(APIService.shared.baseURL)images/profile/\(profile.profilePictureURL)")!) { image in
                image
                    .resizable()
                    .scaledToFit()
                    .frame(width: 50)
                    .clipShape(.circle)
            } placeholder: {
                
            }

            Text(profile.username)
                .font(.title2)
                .fontWeight(.semibold)
            
            Spacer()
            
            if(profile.username != APICommomCalls.shared.currentProfile?.username) {
                Button(isFollowed ? "Unfollow" : "Follow") {
                    Task {
                        if(!isFollowed) {
                            try await follow()
                        } else {
                            try await unfollow()
                        }
                    }
                }
            }
        
        }
        .frame(maxWidth: .infinity, idealHeight: 50, alignment: .leading)
        .padding(.horizontal)
        .onAppear() {
            Task {
                try? await checkFollow()
            }
        }
    }
    
    func checkFollow() async throws {
        let (response, _) = try await APIService.shared.request(
            endpoint: "profile/follow/\(profile.username)",
            method: .GET,
            requiresAuth: true
        ) as (FollowResponse?, Int)
        
        guard let response else {
            return
        }
        
        
        isFollowed = response.isFollowing
        
    }
    
    func follow() async throws {
        let (_, _) = try await APIService.shared.request(
            endpoint: "profile/follow/\(profile.username)",
            method: .POST,
            requiresAuth: true
        ) as (EmptyResponse?, Int)
        
        isFollowed = true;
    }
    
    func unfollow() async throws {
        let (_, _) = try await APIService.shared.request(
            endpoint: "profile/unfollow/\(profile.username)",
            method: .DELETE,
            requiresAuth: true
        ) as (EmptyResponse?, Int)
        
        isFollowed = false
    }

}
