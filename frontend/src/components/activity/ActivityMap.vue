<template>
  <body>
    <div id="map"></div>
  </body>
</template>


<script>
import { ref, onMounted } from "vue";

export default {
  name: "ActivityMap",
  props: {
    location: {
      type: Object,
      required: true,
    },
  },
  setup(props) {
    let map = ref(null);

    const getMap = async () => {
      window.mapIn(initMap);
    };

    const initMap = () => {
      const mapOptions = {
        zoom: 15,
        center: { lat: props.location.latitude, lng: props.location.longitude },
      };

      map.value = new window.google.maps.Map(
        document.getElementById("map"),
        mapOptions
      );

      const marker = new window.google.maps.Marker({
        position: {
          lat: props.location.latitude,
          lng: props.location.longitude,
        },
        map: map.value,
      });

      const infowindow = new window.google.maps.InfoWindow({
        content: "<p>Marker Location:" + props.location.address + "</p>",
      });

      map.value.addListener(marker, "click", () => {
        infowindow.open(map.value, marker);
      });
    };

    onMounted(async () => {
      await getMap();
    });

    return { map };
  },
};
</script>

<style>
#map {
  height: 100%;
}

/* Optional: Makes the sample page fill the window. */
body {
  height: 500px;
  margin: 0;
  padding: 0;
}

@media (max-width: 450px) {
  body {
    height: 400px;
    width: 100%;
    margin: 0 0;
    padding: 0 0;
  }
}
</style>
