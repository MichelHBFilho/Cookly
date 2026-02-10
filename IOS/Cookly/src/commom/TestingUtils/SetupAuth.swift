//
//  SetupAuth.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

func setupAuth() async {
    KeychainService.shared.save("MASTER", to: "refreshToken") // This token only exists on test DB, don't be excited to hack :)
    do {
        try await AuthService.shared.refreshToken()
    } catch {}
}
