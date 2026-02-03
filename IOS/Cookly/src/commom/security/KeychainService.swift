//
//  KeychainService.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

class KeychainService {
    public static var shared = KeychainService()
    
    func save(_ data: String, to key: String) {
        let accessControl = SecAccessControlCreateWithFlags(
            kCFAllocatorDefault,
            kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly,
            .userPresence,
            nil
        )
        
        let query = [
            kSecClass : kSecClassGenericPassword,
            kSecAttrService: key,
            kSecAttrAccount: "account",
            kSecValueData: data.data(using: .utf8)!,
            kSecAttrAccessControl: accessControl as Any,
            kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlocked
        ] as CFDictionary
        
        SecItemDelete(query)
        SecItemAdd(query, nil)
    }
    
    func load(from key: String) throws -> String {
        let query = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: key,
            kSecAttrAccount: "account",
            kSecReturnData: true,
            kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlocked
        ] as CFDictionary
        
        var dataTypeRef: AnyObject?
        
        let status = SecItemCopyMatching(query, &dataTypeRef)
        
        guard status == errSecSuccess,
                let data = dataTypeRef as? Data,
                let info = String(data:data, encoding: .utf8) else {
            throw APIError.NotFound
        }
        
        return info
    }
    
}
