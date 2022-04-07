<template>
  <article class="editField">
    <div>
      <label class="label" :for="id">{{ label }}:</label>
      <p v-if="!edit" :id="id">{{ content.address }}</p>
    </div>

    <input
      :id="id"
      v-if="edit"
      @focus="getLocation"
      type="text"
      class="input is-small"
    />
  </article>
</template>

<style scoped>
article {
  display: block;
}
div {
  display: flex;
  flex-direction: row;
}
label {
  padding-right: 5px;
}
</style>

<script>
export default {
  name: "ActivityFieldLocation",

  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Object,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
    edit: {
      type: Boolean,
      required: false,
    },
  },

  setup(props, { emit }) {
    const options = {
      componentRestrictions: { country: "no" },
    };

    const getLocation = async () => {
      window.mapIn(initLocationSearch);
    };

    const initLocationSearch = () => {
      let autocomplete = new window.google.maps.places.Autocomplete(
        document.getElementById("location"),
        options
      );

      autocomplete.addListener("place_changed", () => {
        const place = autocomplete.getPlace();

        if (place && place.address_components) {
          const tempLoc = {
            address: place.formatted_address,
            latitude: place.geometry.location.lat(),
            longitude: place.geometry.location.lng(),
          };

          const data = { attribute: `${props.id}`, value: tempLoc };
          emit(`update:${props.id}`, data);
        }
      });
    };

    return { getLocation };
  },
};
</script>
