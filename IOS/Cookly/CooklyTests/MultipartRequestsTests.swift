//
//  MultipartRequestsTests.swift
//  CooklyTests
//
//  Created by Michel Filho on 03/02/26.
//

import Foundation
import Testing
@testable import Cookly

struct MultipartRequestsTests {
    
    @Test @MainActor func shouldBuildAWorkingMultipartBody() async throws {
        let form = RegisterRequest(
            username: "michelhbfilho",
            password: "SenhaDoMichel",
            name: "Michel",
            lastName: "Filho",
            birthDay: Date()
        )
        let boundary = UUID().uuidString
        let data = MultipartRequest(images: [], data: form)
        let body = generateMultipartFormDataBody(boundary: boundary, object: data)
        print(String(data:body, encoding: .utf8) ?? "")
    }
    
    @Test @MainActor func shouldDoRequest() async throws {
        let form = RegisterRequest(
            username: "michelhbfilho",
            password: "SenhaDoMichel",
            name: "Michel",
            lastName: "Filho",
            birthDay: Date()
        )
        
        let viewModel = RegisterViewModel()
        viewModel.form = form
        
        do {
            try await viewModel.doRequest()
        } catch {
            print(error)
        }
    }
    
}
