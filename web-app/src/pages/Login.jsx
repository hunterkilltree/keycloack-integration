import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  TextField,
  Typography,
} from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../services/localStorageService";
import keycloak from "../keycloak";

export default function Login() {
  const navigate = useNavigate();

  const handleNavigateRegistration = () => {
    navigate("/registration");
  };

  const handleClickGoogle = () => {
    // Trigger login with Google
    keycloak
      .login({
        idpHint: "google", // Use Google as the identity provider
      })
      .then(() => {
        console.log("login success");
      })
      .catch((error) => {
        console.error("Keycloak login with Google failed", error);
      });
  };

  useEffect(() => {
    const accessToken = getToken();
    console.log("accessToken", accessToken);
    if (accessToken) {
      navigate("/");
    }

    if (keycloak.authenticated) {
      navigate("/profile");    
    }
    
  }, [navigate]);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    const loginData = {
      grant_type: 'password',
      client_id: "webapp_germany",
      username: username,
      password: password,
    };

    // console.log("Login data:", loginData);
  
    try {
      const response = await fetch(`http://localhost:8180/realms/hunterkilltree/protocol/openid-connect/token`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(loginData),
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Access Token:', data.access_token);
        // Lưu access token vào localStorage hoặc xử lý theo yêu cầu
        keycloak.token = data.access_token;
        keycloak.refreshToken = data.refresh_token;
        keycloak.authenticated = true;

        // navigate to new page
        navigate('/profile');
      } else {
        console.error('Login failed');
      }
    } catch (error) {
      console.error('Error during login:', error);
    }
  };

  return (
    <>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        height="100vh"
        bgcolor={"#f0f2f5"}
      >
        <Card
          sx={{
            minWidth: 250,
            maxWidth: 400,
            boxShadow: 4,
            borderRadius: 4,
            padding: 4,
          }}
        >
          <CardContent>
            <Typography variant="h5" component="h1" gutterBottom>
              Welcome to My App
            </Typography>
            <Box component="form" onSubmit={handleLogin} sx={{ mt: 2 }}>
              <TextField
                label="Username"
                variant="outlined"
                fullWidth
                margin="normal"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <TextField
                label="Password"
                type="password"
                variant="outlined"
                fullWidth
                margin="normal"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Box>
          </CardContent>
          <CardActions>
            <Box display="flex" flexDirection="column" width="100%" gap="25px">
              <Button
                type="submit"
                variant="contained"
                color="primary"
                size="large"
                fullWidth
                onClick={handleLogin}
              >
                Login
              </Button>
              <Button
                type="button"
                variant="contained"
                color="secondary"
                size="large"
                onClick={handleClickGoogle}
                fullWidth
                sx={{ gap: "10px" }}
              >
                <GoogleIcon />
                Continue with Google
              </Button>
              <Divider></Divider>
              <Button
                type="submit"
                variant="contained"
                color="success"
                size="large"
                onClick={handleNavigateRegistration}
              >
                Create an account
              </Button>
            </Box>
          </CardActions>
        </Card>
      </Box>
    </>
  );
}
