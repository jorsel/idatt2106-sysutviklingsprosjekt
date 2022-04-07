<template>
  <textarea
    class="my_textarea"
    :id="id"
    :required="required"
    :rows="rows"
    :cols="cols"
    v-model="content"
  />
</template>

<script>
import { ref, defineComponent } from "vue";

export default defineComponent({
  name: "TextArea",
  props: {
    id: {
      type: String,
      required: true,
    },
    required: {
      type: Boolean,
      required: false,
      default: false,
    },
    value: {
      required: false,
    },
    rows: {
      type: Number,
      required: false,
    },
    cols: {
      type: Number,
      required: false,
    },
  },
  setup(props) {
    const content = ref(props.value);

    return { content };
  },
});
</script>

<style scoped>
.my_textarea {
  resize: vertical;
  padding: 0.25rem;
  margin: 0.5rem;
  min-height: 3rem;
  max-height: 12rem;
  width: 97%;
}
</style>
