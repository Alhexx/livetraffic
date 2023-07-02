import "leaflet/dist/leaflet.css";
import L from "leaflet";
import styles from "./style.module.scss";
import api from "../../services/api";
import { useEffect, useState } from "react";
import { ListGroup, Offcanvas, Button } from "react-bootstrap";
import { MapContainer, Marker, Popup, TileLayer } from "react-leaflet";

export default function Map() {
  const [isLoading, setIsLoading] = useState(false);
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const [sensores, setSensores] = useState([]);

  const markerRed = new L.Icon({
    iconUrl: "/pin.png",
    iconSize: [25, 40],
    iconAnchor: [12, 40],
    popupAnchor: [0, -40],
  });
  const markerGreen = new L.Icon({
    iconUrl: "/pin2.png",
    iconSize: [25, 40],
    iconAnchor: [12, 40],
    popupAnchor: [0, -40],
  });
  const markerYellow = new L.Icon({
    iconUrl: "/pin3.png",
    iconSize: [25, 40],
    iconAnchor: [12, 40],
    popupAnchor: [0, -40],
  });

  async function getData() {
    try {
      setIsLoading(true);
      const res = await api.get("/sensors");

      const dados = res.data.map((item) => ({
        coordinates: item.location.value.coordinates,
        latitude: item.location.value.coordinates[0],
        longitude: item.location.value.coordinates[1],
        status: item.flow.value,
        id: item.id,
      }));
      console.log(dados);

      setSensores(dados);

      setIsLoading(false);
    } catch (e) {
      console.log("Erro de fetch");
      setIsLoading(false);
    }
  }

  useEffect(() => {
    getData();
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.contact}>
        <div id="contact-form-overlay-mini">
          <Button variant="dark" onClick={handleShow}>
            Sensors
          </Button>

          <Offcanvas show={show} onHide={handleClose} placement="end">
            <Offcanvas.Header closeButton>
              <Offcanvas.Title>List</Offcanvas.Title>
            </Offcanvas.Header>
            <Offcanvas.Body>
              <Button variant="dark" onClick={handleShow}>
                Sensors
              </Button>
              <ListGroup>
                <ListGroup.Item action onClick={console.log("FOI")}>
                  SENSOR 1
                </ListGroup.Item>
                <ListGroup.Item action onClick={console.log("FOI")}>
                  SENSOR 2
                </ListGroup.Item>
                <ListGroup.Item action onClick={console.log("FOI")}>
                  SENSOR 3
                </ListGroup.Item>
              </ListGroup>
            </Offcanvas.Body>
          </Offcanvas>
        </div>
      </div>
      <MapContainer
        center={[-5.832430084556201, -35.205416846609594]}
        zoom={15}
        style={{ height: "77vh", width: "100%" }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {sensores.map((sensor, i) => (
          <Marker
            position={sensor.coordinates}
            icon={
              sensor.status == "free"
                ? markerGreen
                : sensor.status == "moderate"
                ? markerYellow
                : markerRed
            }
          >
            <Popup>
              <div>
                <h5
                  style={{
                    justifyContent: "center",
                    display: "flex",
                  }}
                >
                  Status:{" "}
                  <label
                    style={
                      sensor.status == "free"
                        ? { color: "green", marginLeft: "5px" }
                        : sensor.status == "moderate"
                        ? { color: "yellow", marginLeft: "5px" }
                        : { color: "red", marginLeft: "5px" }
                    }
                  >
                    {sensor.status}{" "}
                  </label>
                </h5>
                <br />
                <span style={{ marginRight: "10px" }}>
                  Latitude: {sensor.latitude}
                </span>
                <span>Longitude: {sensor.longitude}</span>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}
