import Router from "next/router";
import Image from "next/image";
import { useEffect, useState } from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";

export function Menu() {
  // const [isLoading, setIsLoading] = useState(false);
  // const [logado, setLogado] = useState(false);

  // useEffect(() => {
  //   setLogado(localStorage.getItem("id") != null ? true : false);
  // }, []);

  return (
    <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
      <Container>
        <Navbar.Brand onClick={() => Router.push("/")} as="a">
          <Image
            src="/joe.png"
            alt={"joe-logo"}
            height={100}
            width={120}
          ></Image>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="me-auto"></Nav>
          <Nav>
            <Nav.Link onClick={() => Router.push("/login")} as="a">
              Login
            </Nav.Link>
            <Nav.Link onClick={() => Router.push("/register")} as="a">
              Registrar-se
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
