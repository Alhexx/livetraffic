import Head from "next/head";
import styles from "../styles/Home.module.css";
import { Container } from "react-bootstrap";
import Map from "../components/Map";

export default function Home() {
  return (
    <div className={styles.container}>
      <Container>
        <Map />
      </Container>
    </div>
  );
}
