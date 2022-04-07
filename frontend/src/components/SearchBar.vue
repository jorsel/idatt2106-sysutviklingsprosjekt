<template>
  <div class="field has-addons">
    <p class="control">
      <span class="select">
        <select @input="updateValue">
          <option value="search">Navn</option>
          <option value="dates">Dato</option>
          <option value="tags">Tags</option>
          <option value="intensities">Aktivitetsnivå</option>
        </select>
      </span>
    </p>

    <p class="control">
      <input
        v-model="inputString"
        class="input"
        :list="autoCompleteList"
        :name="name"
        :type="type"
        @keyup.enter="emitFunc"
        onkeypress="return /[a-z0-9]/i.test(event.key)"
      />

      <datalist id="intensityList">
        <option value="Lav">Lav</option>
        <option value="Medium">Medium</option>
        <option value="Høy">Høy</option>
      </datalist>

      <datalist id="tagsList">
        <option v-for="(tag, index) in tagArray" :key="index" :value="tag.tag">
          {{ tag.tag }}
        </option>
      </datalist>
    </p>

    <p class="control">
      <a class="button" @click="emitFunc">Søk</a>
    </p>
  </div>
</template>

<script>
import { ref } from "vue";
export default {
  name: "SearchBar",
  components: {},
  props: {
    propTags: String,
  },
  setup(props, { emit }) {
    const type = ref("text");
    const name = ref("");
    const autoCompleteList = ref("");

    const queryType = ref("search");
    const inputString = ref("");
    const tagArray = JSON.parse(props.propTags);

    const updateValue = (option) => {
      const optionValue = option.target.value;
      queryType.value = optionValue;

      switch (optionValue) {
        case "dates":
          type.value = "date";
          name.value = "";
          autoCompleteList.value = "";
          break;
        case "tags":
          type.value = "text";
          name.value = "tags";
          autoCompleteList.value = "tagsList";
          break;
        case "search":
          type.value = "text";
          name.value = "";
          autoCompleteList.value = "";
          break;
        case "intensities":
          type.value = "text";
          name.value = "intensity";
          autoCompleteList.value = "intensityList";
          break;
      }
    };

    const emitFunc = () => {
      emit("update:search", { type: queryType, value: inputString.value });
      inputString.value = "";
    };

    return {
      type,
      updateValue,
      name,
      autoCompleteList,
      emitFunc,
      inputString,
      tagArray,
    };
  },
};
</script>