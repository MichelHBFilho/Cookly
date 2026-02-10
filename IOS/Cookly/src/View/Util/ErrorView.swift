//
//  ErrorView.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct ErrorView: View {
    let error : Error
    var body: some View {
        // TODO
    }
}

#Preview {
    ErrorView(error: APIError.NotFound)
}
