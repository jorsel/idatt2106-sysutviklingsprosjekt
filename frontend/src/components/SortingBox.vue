<template>
  <div class="field has-addons">
    <p class="control">
      <span class="select">
        <select @input="sortByType">
          <option value="created">Sist opprettet</option>
          <option value="title">Tittel</option>
          <option value="date">Dato</option>
          <option value="intensity">Aktivitetsnivå</option>
        </select>
      </span>
    </p>

    <p class="control">
      <a class="button" @click="setAscDesc" v-if="!!sortQuery">
        <p v-if="ascending">Stigende &#9650;</p>
        <p v-if="!ascending">Synkende &#9660;</p>
      </a>
    </p>

    <p class="control">
      <a class="button" @click="sortClick">Sorter</a>
    </p>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  setup(props, { emit }) {
    let ascending = ref(false);
    let queryType = ref("");
    let sortQuery = ref("");

    const setAscDesc = () => {
      ascending.value = !ascending.value;
      sortEmit();
    };

    const sortByType = (option) => {
      queryType.value = option.target.value;
      sortEmit();
    };
    const sortEmit = () => {
      if (queryType.value === "created") sortQuery.value = "";
      // Sorting Title
      if (queryType.value === "title" && ascending.value === true) {
        sortQuery.value = "title_asc";
      } else if (queryType.value === "title" && ascending.value === false) {
        sortQuery.value = "title_desc";
      }
      // Sorting Date
      if (queryType.value === "date" && ascending.value === true) {
        sortQuery.value = "start_asc";
      } else if (queryType.value === "date" && ascending.value === false) {
        sortQuery.value = "start_desc";
      }
      // Sorting Intensity
      if (queryType.value === "intensity" && ascending.value === true) {
        sortQuery.value = "intensity_asc";
      } else if (queryType.value === "intensity" && ascending.value === false) {
        sortQuery.value = "intensity_desc";
      }
    };
    const sortClick = () => {
      emit("update:sort", { desc: sortQuery });
    };
    return {
      setAscDesc,
      sortByType,
      sortClick,
      ascending,
      sortQuery,
    };
  },
};
</script>

<style scoped>
</style>
