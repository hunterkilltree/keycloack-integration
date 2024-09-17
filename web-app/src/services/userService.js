import httpClient from "../configurations/httpClient";
import { API } from "../configurations/configuration";
import keycloak from "../keycloak";


/**
 * Registers a new user with the given information.
 *
 * @param {Object} data User data to be registered.
 * @property {string} data.username The username of the user.
 * @property {string} data.password The password of the user.
 * @property {string} [data.firstName] The first name of the user.
 * @property {string} [data.lastName] The last name of the user.
 * @property {string} [data.email] The email of the user.
 *
 * @returns {Promise} Resolves with the result of the registration.
 */
export const register = async (data) => {
  return await httpClient.post(API.REGISTRATION, data);
};

export const storeMyProfile = async (data) => {
  return await httpClient.post(API.MY_PROFILE, data, {
    headers : {
      Authorization: "Bearer " + keycloak.token
    }
  })
}

export const getMyProfile = async () => {
  console.log("AccessToken:", keycloak.token);
  
  return await httpClient.get(API.MY_PROFILE, {
    headers : {
      Authorization: "Bearer " + keycloak.token
    }
  })
}