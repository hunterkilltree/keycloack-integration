import React from "react";
import { Navigate } from "react-router-dom";
import keycloak from "../keycloak";

const ProtectedRoute = () => {
  console.log("Keycloak authenticated:", keycloak.authenticated);
  
  return keycloak.authenticated ? <Navigate to="/profile" /> : <Navigate to="/login" />;
};

export default ProtectedRoute;
