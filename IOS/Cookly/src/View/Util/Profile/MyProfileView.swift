//
//  MyProfileView.swift
//  Cookly
//
//  Created by Michel Filho on 08/02/26.
//

import SwiftUI

struct MyProfileView: View {
    @State private var profile: Profile? = nil
    var body: some View {
        HStack {
            if let profile {
                AsyncImage(url: URL(string: "\(APIService.shared.baseURL)images/profile/\(profile.profilePictureURL)")) { image in
                    image
                        .resizable()
                        .scaledToFit()
                        .frame(height: 150)
                        .clipShape(Circle())
                } placeholder: {
                }
                Text(profile.username)
                    .font(.title)
                    .fontWeight(.semibold)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .task {
            do {
                let (profileResponse, _) = try await APIService.shared.request(
                    endpoint: "profile",
                    method: .GET,
                    requiresAuth: true
                ) as (ProfileResponse?, Int)
                
                profile = ProfileResponse.toProfileObject(profile: profileResponse!)
            } catch {}
        }
    }
}

#Preview {
    MyProfileView()
}
