<template>
  <article class="editField">
    <label class="label" :for="id">{{ label }}:</label>
    <textarea
      :id="id"
      v-if="edit"
      :value="content"
      @input="updateContent"
      class="input"
    />
    <p v-else :id="id">{{ content }}</p>
  </article>
</template>

<style scoped>
article {
  display: block;
}
textarea {
  resize: vertical;
  min-height: 3rem;
  max-height: 10rem;
  padding: 0.25rem;
}
</style>

<script>
export default {
  name: "ActivityFieldText",

  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: String,
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
      const data = { attribute: `${props.id}`, value: event.target.value };
      emit(`update:${props.id}`, data);
    };
    return { updateContent };
  },
};
</script>
