//
//  DataExtension.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

extension Data {
    mutating public func append(_ string: String) {
        if let data = string.data(using: .utf8) {
            self.append(data)
        }
    }
}
