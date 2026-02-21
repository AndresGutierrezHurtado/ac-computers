"use client";

import React, { useEffect } from "react";
import { MapContainer, Marker, Popup, TileLayer, useMapEvent } from "react-leaflet";

import "leaflet/dist/leaflet.css";

const markerIcon = new L.Icon({
    iconUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png",
    iconSize: [25, 41], // Tamaño del icono
    iconAnchor: [12, 41], // Punto de anclaje
    popupAnchor: [1, -34], // Ajuste del popup
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png",
    shadowSize: [41, 41], // Tamaño de la sombra
});

function MyComponent() {
    useMapEvent("click", (e) => {
        console.log(e.latlng.toString());
    });
    return null;
}

export default () => {
    const position = [4.6642, -74.0589];

    return (
        <MapContainer
            center={position}
            zoom={13}
            scrollWheelZoom={true}
            style={{ height: "500px", width: "100%" }}
        >
            <TileLayer
                attribution={`&copy; <a href="${process.env.NEXT_PUBLIC_APP_DOMAIN}">AC Computers</a>`}
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={position} icon={markerIcon}>
                <Popup>
                    <p className="font-bold ">Ubicación de <span className="text-primary">AC Computers</span></p>
                </Popup>
            </Marker>
            <MyComponent />
        </MapContainer>
    );
};
