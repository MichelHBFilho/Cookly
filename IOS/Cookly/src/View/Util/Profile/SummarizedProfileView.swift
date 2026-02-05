//
//  SummarizedProfileView.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct SummarizedProfileView : View {
    
    let profile: Profile
    
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
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal)
    }

}

#Preview {
    let profile = Profile(
        fullName: "Mari Schultz",
        username: "marischultz",
        birthday: Date.distantPast,
        profilePictureURL: "3321e014-f481-4324-b911-3670cf8f9c41_DSCN2826 Small.jpeg"
    )
    
    SummarizedProfileView(profile:profile)
}
