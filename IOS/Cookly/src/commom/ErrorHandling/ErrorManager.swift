//
//  ErrorManager.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

@Observable
class ErrorManager {
    static var shared = ErrorManager()
    
    var currentError : Error?
    var showError: Bool = false
    
    func handle(_ error : Error) {
        currentError = error
        showError = true
    }
    
    func clear() {
        currentError = nil
        showError = false
    }
}
