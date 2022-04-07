<template>
  <article class="editField">
    <label class="label" :for="id">{{ label }}:</label>
    <input
      :id="id"
      v-if="edit"
      :value="content"
      @input="updateContent"
      type="number"
      class="input"
    />
    <p v-else :id="id">{{ content }}</p>
  </article>
</template>

<style scoped>
article {
  display: block;
}
p {
  padding: 0;
  margin: 0 4px;
}
</style>

<script>
export default {
  name: "ActivityFieldNumber",

  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Number,
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
    const updateContent = (event) => {
      const data = {
        attribute: `${props.id}`,
        value: parseFloat(event.target.value),
      };
      emit(`update:${props.id}`, data);
    };
    return { updateContent };
  },
};
</script>
