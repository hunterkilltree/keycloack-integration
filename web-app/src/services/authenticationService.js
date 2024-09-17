import { removeToken } from "./localStorageService";
import keycloak from "../keycloak";

export const logOut = () => {
  removeToken();
  // keycloak.logout();
  keycloak.logout({
    redirectUri: window.location.origin,  // Tùy chọn: Chuyển hướng sau khi logout
  }).then(() => {
    // Xóa token khỏi localStorage hoặc sessionStorage nếu cần
    localStorage.removeItem('kc-token');
    localStorage.removeItem('kc-refresh-token');
    
    // Hoặc nếu bạn dùng sessionStorage
    sessionStorage.removeItem('kc-token');
    sessionStorage.removeItem('kc-refresh-token');

    console.log("User logged out successfully");
  }).catch((error) => {
    console.error('Failed to logout', error);
  });
};
