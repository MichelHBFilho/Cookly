//
//  APIService.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import Foundation

class APIService {
    public static var shared = APIService()
    
    private let baseURL = "http://localhost:8080/" // Change this if in production
    
    public func request<T: Decodable>(
        endpoint : String,
        method : HTTPMethod = .GET,
        requiresAuth : Bool = true,
        body : Encodable? = nil,
        authToken: String? = nil // Just for the first refresh Token request
    ) async throws -> T {
        guard let url = URL(string: baseURL + endpoint) else {
            throw APIError.URLInvalid
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("MobileApp", forHTTPHeaderField: "User-Agent")
        
        if requiresAuth && authToken == nil {
            try await AuthService.shared.setAuthHeader(&request)
        }
        if let authToken {
            request.setValue("Bearer " + authToken, forHTTPHeaderField: "Authorization")
        }
        
        if let body {
            request.httpBody = try JSONEncoder().encode(body)
        }
    
        let (data, response) = try await URLSession.shared.data(for: request)
        
        try handleAPIResponseCode(httpResponse: (response as? HTTPURLResponse)!)
        
        return try JSONDecoder().decode(T.self, from: data)
    }
    
    public func multipartRequest<T: Decodable>(
        endpoint : String,
        method : HTTPMethod = .POST,
        requiresAuth : Bool = true,
        body : Data
    ) async throws -> T {
        guard let url = URL(string: baseURL + endpoint) else {
            throw APIError.URLInvalid
        }
        let boundary = UUID().uuidString
        
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.setValue("MobileApp", forHTTPHeaderField: "User-Agent")
        
        if requiresAuth {
            try await AuthService.shared.setAuthHeader(&request)
        }
        
        request.httpBody = body
    
        let (data, response) = try await URLSession.shared.data(for: request)
        
        try handleAPIResponseCode(httpResponse: (response as? HTTPURLResponse)!)
        
        return try JSONDecoder().decode(T.self, from: data)
    }
    
    private func handleAPIResponseCode(httpResponse : HTTPURLResponse) throws {
        switch(httpResponse.statusCode) {
        case 200...299:
            break
        case 403:
            throw APIError.Unauthenticated
        case 404:
            throw APIError.NotFound
        case 500...599:
            throw APIError.ServerError
        default:
            break
        }
    }
}
