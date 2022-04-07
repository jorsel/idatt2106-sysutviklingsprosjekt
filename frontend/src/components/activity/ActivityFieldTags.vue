<template>
  <article class="wrap">
    <div class="editField" id="editField">
      <label class="label" :for="id">{{ label }}:</label>

      <p class="control" v-if="edit">
        <span class="select">
          <select :value="content" :id="id" @input="updateContent">
            <option class="" disabled value="" selected>Velg en tag:</option>
            <option
              v-for="(tag, index) in tagList"
              :key="index"
              :value="tag.tag"
            >
              {{ tag.tag }}
            </option>
          </select>
        </span>
      </p>
    </div>

    <div class="columns block-display">
      <div class="column remove-padding" :id="id" v-for="(tag, index) in content" :key="index">
        <ActivityTag
          :edit="edit"
          :tagName="tag.tag"
          @update:remove="listenOnRemoveTag"
          v-on:click="emitClicked(tag.tag)"
        />
      </div>
    </div>
  </article>
</template>

<style scoped>
.remove-padding {
  padding: 0;
}
.block-display {
  display: block;
}
#editField {
  display: block;
}
#wrap {
  display: flex;
  flex-direction: column;
}
.select {
  width: 100%;
}
select {
  width: inherit;
}
</style>

<script>
import ActivityTag from "@/components/activity/ActivityTag.vue";
export default {
  name: "ActivityFieldTags",

  components: {
    ActivityTag,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    content: {
      type: Array,
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
    tagList: {
      type: Array,
      required: true,
    },
  },
  setup(props, { emit }) {
    const updateContent = (event) => {
      const data = {
        attribute: `${props.id}`,
        value: { tag: event.target.value },
      };
      emit(`update:${props.id}`, data);
    };

    const listenOnRemoveTag = (tagObj) => {
      emit("update:removeTag", tagObj);
    };
    const emitClicked = (clickedTag) => {
      emit("tag:clicked", { tag: clickedTag });
    };
    return { updateContent, listenOnRemoveTag, emitClicked };
  },
};
</script>
