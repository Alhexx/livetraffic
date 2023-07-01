import "leaflet/dist/leaflet.css";
import L from "leaflet";
import { MapContainer, Marker, Popup, TileLayer } from "react-leaflet";

export default function Map() {
  const markerIcon = new L.Icon({
    iconUrl: "/pin.png",
    iconSize: [25, 40],
    iconAnchor: [12, 40],
    popupAnchor: [0, -40],
  });

  return (
    <MapContainer
      center={[-5.832430084556201, -35.205416846609594]}
      zoom={15}
      style={{ height: "30rem", width: "100%" }}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker
        position={[-5.823309251029565, -35.20485960490109]}
        icon={markerIcon}
      >
        <Popup>CASA DE ALEXANDRE</Popup>
      </Marker>
      <Marker
        position={[-5.829277421339476, -35.224847178878264]}
        icon={markerIcon}
      >
        <Popup>CASA DE GALVAO</Popup>
      </Marker>
      <Marker
        position={[-5.831808788033889, -35.205509895072694]}
        icon={markerIcon}
      >
        <Popup>LAR DE JAIR LEITE</Popup>
      </Marker>
      <Marker position={[27.80059, 70.42253]} icon={markerIcon}>
        <Popup>LAR DE JAIR LEITE</Popup>
      </Marker>
    </MapContainer>
  );
}
