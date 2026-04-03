//
//  ProfileResponse.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

struct ProfileResponse : Decodable {
    let profilePictureURL: String
    let birthDay: String
    let fullName: String
    let username: String
    
    func toProfile() -> Profile {
        let dateFormatter = ISO8601DateFormatter()
        return Profile(
            fullName: fullName,
            username: username,
            birthday: dateFormatter.date(from: birthDay) ?? Date(),
            profilePictureURL: profilePictureURL
        )
    }
}

struct FollowResponse : Decodable {
    let isFollowing: Bool
}
