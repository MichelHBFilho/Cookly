//
//  APICommomCalls.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

class APICommomCalls {
    public static let shared = APICommomCalls()
    
    func getProfileByUsername(_ username: String) async -> Profile {
        do {
            let (profile, _) = try await APIService.shared.request(
                endpoint: "profile/\(username)",
                method: .GET,
                requiresAuth: true
            ) as (ProfileResponse?, Int)
            
            let dateFormatter = ISO8601DateFormatter()
            
            guard let profile else { throw APIError.NotFound }
            
            return ProfileResponse.toProfileObject(profile: profile)
        } catch {
            ErrorManager.shared.handle(error)
        }
        return Profile(fullName: "", username: "", birthday: Date(), profilePictureURL: "")
    }
    
    func getCurrentProfile() async -> Profile {
        do {
            let (profile, _) = try await APIService.shared.request(
                endpoint: "profile",
                method: .GET,
                requiresAuth: true
            ) as (ProfileResponse?, Int)
            
            let dateFormatter = ISO8601DateFormatter()
            
            guard let profile else { throw APIError.NotFound }
            
            return ProfileResponse.toProfileObject(profile: profile)
        } catch {
            ErrorManager.shared.handle(error)
        }
        return Profile(fullName: "", username: "", birthday: Date(), profilePictureURL: "")
    }
}
