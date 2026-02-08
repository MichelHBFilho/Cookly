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
    
    static func toProfileObject(profile: ProfileResponse) -> Profile {
        let dateFormatter = ISO8601DateFormatter()
        return Profile(
            fullName: profile.fullName,
            username: profile.username,
            birthday: dateFormatter.date(from: profile.birthDay) ?? Date(),
            profilePictureURL: profile.profilePictureURL
        )
    }
}
