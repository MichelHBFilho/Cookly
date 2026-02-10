//
//  MyProfileView.swift
//  Cookly
//
//  Created by Michel Filho on 08/02/26.
//

import SwiftUI

struct MenuView: View {
    @State private var profile: Profile? = nil
    @State private var showLogoutPopup: Bool = false
    var body: some View {
        HStack {
            if let profile {
                AsyncImage(url: URL(string: "\(APIService.shared.baseURL)images/profile/\(profile.profilePictureURL)")) { image in
                    image
                        .resizable()
                        .scaledToFit()
                        .frame(height: 150)
                        .clipShape(Circle())
                } placeholder: {
                }
                VStack(spacing: 0) {
                    Text(profile.username)
                        .font(.title)
                        .fontWeight(.semibold)
                    
                    HStack {
                        MyButton(
                            title: "New Post",
                            color: .cooklyBlue
                        ) {
                            Router.shared.route = .newPost
                        }
                        
                        Button {
                            showLogoutPopup = true
                        } label: {
                            Image(systemName: "rectangle.portrait.and.arrow.right")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 30)
                                .foregroundStyle(.cooklyRed)
                        }

                    }
                }
            }
        }
        .alert(
            "Are you sure?",
            isPresented: $showLogoutPopup,
            actions: {
                MyButton(
                    title: "Yes",
                    color: .cooklyBlue) {
                        AuthService.shared.logout()
                }
                
                MyButton(
                    title: "No",
                    color: .cooklyBlue) {
                        showLogoutPopup = false
                }
            }
        )
        .frame(maxWidth: .infinity, alignment: .leading)
        .task {
            profile = await APICommomCalls.shared.getCurrentProfile()
        }
    }
}

#Preview {
    MenuView()
}
