//
//  Router.swift
//  Cookly
//
//  Created by Michel Filho on 08/02/26.
//

import Foundation
import Combine
class Router : ObservableObject {
    static var shared = Router()
    
    enum AppRoute {
        case authentication
        case homepage
        case newPost
    }
    
    @Published var route: AppRoute = .authentication
}
