<template>
  <section v-if="details" class="sect">
    <Button @click="activityDetailsBack">Tilbake</Button>
    <ActivityDetails
      :id="selectedActivity.id"
      :title="selectedActivity.title"
      :description="selectedActivity.description"
      :location="selectedActivity.location"
      :start="selectedActivity.start"
      :end="selectedActivity.end"
      :equipment="selectedActivity.equipment"
      :weather="selectedActivity.weather"
      :participants="selectedActivity.participants"
      :maxAmount="selectedActivity.maxAmount"
      :waitinglist="selectedActivity.waitingList"
      :intensity="selectedActivity.intensity"
      :tags="selectedActivity.tags"
      :tagList="selectedActivity.tagList"
      :createdBy="selectedActivity.createdBy"
      @update:save="listenOnSave"
      @delete:activity="listenOnDelete"
    />

    <div v-show="logged">
      <ActivityJoin
        :selectedActivity="selectedActivity"
        :joinedActivities="joinedActivities"
        @update:participation="listenOnParticipation"
      />
    </div>

    <article class="comments" v-show="logged">
      <TextArea
        id="commentText"
        name="commentText"
        v-model="msg"
        maxlength="360"
        placeholder="Skriv kommentaren din her!"
      >
      </TextArea>
      <Button @click="submitComment">Send kommentar</Button>
    </article>

    <article
      class="comments"
      v-for="(comment, index) in comments.value"
      :key="index"
    >
      <Comment
        :name="comment.user.firstName"
        :text="comment.message"
        :time="new Date(comment.dateTime)"
      />
    </article>
  </section>

  <section v-else class="sect">
    <div
      class="notification is-info"
      @mouseover="hideMessage"
      v-if="msgVar !== ''"
    >
      {{ msgVar }}
    </div>
    <div id="topDiv">
      <h1 class="title">Aktivitets feed</h1>
      <Button v-on:click="createActivity" v-show="logged" class="create">
        Lag Aktivitet</Button
      >
      <SortingBox class="sortingBox" @update:sort="listenOnSort" />
      <SearchBar
        class="searchbar"
        v-bind:propTags="JSON.stringify(selectedActivity.tagList)"
        v-if="loadBool"
        @update:search="listenOnSearch"
      />
      <div class="sortWrapper">
        <section id="filterParams">
          <article v-for="(filter, index) in filterStringArray" :key="index">
            <Button class="filterButton"
              >{{ filter
              }}<button
                class="delete"
                @click="removeFilterParamAndSearch(filter)"
              ></button
            ></Button>
          </article>
        </section>
      </div>
    </div>

    <section>
      <article v-for="(activity, index) in activities" :key="index">
        <br />
        <ActivityPreview
          @tag:clicked="listenOnTagClick"
          @click="activityDetails(activity)"
          :title="activity.title"
          :description="activity.description"
          :location="activity.location"
          :start="activity.start"
          :end="activity.end"
          :equipment="activity.equipment"
          :weather="activity.weather"
          :participants="activity.participants"
          :maxAmount="activity.maxAmount"
          :waitinglist="activity.waitingList"
          :intensity="activity.intensity"
          :tags="activity.tags"
        />
      </article>
    </section>
    <nav class="pagination is-medium" role="navigation" aria-label="pagination">
      <ul class="pagination-list">
        <li>
          <a
            class="pagination-link"
            aria-label="Goto page1 "
            v-if="currentPage !== 1"
            @click="changePage(1)"
            >1</a
          >
        </li>
        <li>
          <span class="pagination-ellipsis" v-if="currentPage > 3"
            >&hellip;</span
          >
        </li>
        <li>
          <a
            class="pagination-link"
            :aria-label="'Goto page' + currentPage - 1"
            v-if="currentPage > 2"
            @click="changePage(currentPage - 1)"
            >{{ currentPage - 1 }}</a
          >
        </li>
        <li>
          <a
            class="pagination-link is-current"
            :aria-label="'Page' + currentPage"
            aria-current="page"
            >{{ currentPage }}</a
          >
        </li>
        <li>
          <a
            class="pagination-link"
            :aria-label="'Goto page' + currentPage + 1"
            v-if="currentPage < nPages - 1"
            @click="changePage(currentPage + 1)"
            >{{ currentPage + 1 }}</a
          >
        </li>
        <li>
          <span class="pagination-ellipsis" v-if="currentPage < nPages - 2"
            >&hellip;</span
          >
        </li>
        <li>
          <a
            class="pagination-link"
            :aria-label="'Goto page' + nPages"
            v-if="currentPage !== nPages && nPages !== 1 && nPages !== 0"
            @click="changePage(nPages)"
            >{{ nPages }}</a
          >
        </li>
      </ul>
    </nav>
  </section>
</template>

<script>
import ActivityPreview from "@/components/activity/ActivityPreview.vue";
import ActivityDetails from "@/components/activity/ActivityDetails.vue";
import ActivityJoin from "@/views/activity/ActivityJoin.vue";
import SearchBar from "@/components/SearchBar.vue";
import SortingBox from "@/components/SortingBox.vue";
import { reactive, ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import Button from "@/components/common/Button";
import Comment from "@/components/common/Comment";
import TextArea from "@/components/common/TextArea";
import { DELETE, GET, PUT, baseURL, request } from "@/utility/request.js";

export default {
  name: "Activities",
  components: {
    Button,
    ActivityPreview,
    ActivityDetails,
    SearchBar,
    SortingBox,
    Comment,
    TextArea,
    ActivityJoin,
  },
  props: {
    msgActCreate: {
      type: String,
      required: false,
    },
  },
  setup(props) {
    const joinedActivities = ref([]);
    const logged = ref(false);
    const activities = reactive([]);
    const filterParams = new Map();
    //Array used to store the text in the filterbuttons
    const filterStringArray = reactive([]);
    let msg = ref("");
    const comments = reactive([]);
    // for translating searches between english dtb and norwegian ui
    let translationMap = new Map();
    let intensityArr = new Array();
    let loadBool = ref(false);
    const msgVar = ref("");
    const currentPage = ref(1);
    const nPages = ref(1);

    translationMap.set("intensities", "Aktivitetsnivå");
    translationMap.set("dates", "Dato");
    translationMap.set("tags", "Tags");
    translationMap.set("search", "Navn");

    intensityArr.push("Lav");
    intensityArr.push("Medium");
    intensityArr.push("Høy");

    const router = useRouter();
    const createActivity = () => {
      router.push({ path: "/activity/create" });
    };

    const getTags = async () => {
      const response = await request(`${baseURL}/tags`, GET);

      if (response.status === 200) {
        selectedActivity.tagList = response.data;
      } else {
        console.error("An error occured while fetching activity tags!");
      }
    };

    const changePage = (page) => {
      currentPage.value = page;
      searchAxios();
    };

    const listenOnSave = async (obj) => {
      const response = await request(
        `${baseURL}/activity/${obj.id}`,
        PUT,
        JSON.stringify(obj)
      );
      if (response.status === 200) {
        replaceActivity(response.data);
      } else {
        console.error("And error occured while updating activity!");
      }
    };

    const listenOnDelete = async (id) => {
      const response = await request(
        `${baseURL}/activity/${id}`,
        DELETE,
        JSON.stringify({ id: id })
      );

      if (response.status === 200) {
        let indexAct = activities.findIndex(hasSameId);
        if (indexAct > -1) {
          activities.splice(indexAct, 1);
        }
      } else {
        console.error("An error occured while deleting activity!");
      }
      details.value = false;
    };

    let tempSearchQuery = ref({});

    const listenOnSearch = (obj) => {
      Object.assign(tempSearchQuery.value, obj);
      search();
    };

    //this is for managing the tag:clicked event, as it will also always call activitydetails.
    const tagclicked = ref(false);

    const listenOnTagClick = (obj) => {
      tagclicked.value = true;
      addParamAndSearch("tags", obj.tag);
    };

    let tempSortQuery = ref({});

    //is called when sort button is pressed
    const listenOnSort = (obj) => {
      Object.assign(tempSortQuery.value, obj);
      searchAxios();
    };

    //This is for pressing the search button, does not respond if search has empty value
    const search = () => {
      if (
        tempSearchQuery.value.value !== undefined &&
        tempSearchQuery.value.value !== ""
      ) {
        addParamAndSearch(
          tempSearchQuery.value.type,
          tempSearchQuery.value.value
        );
      }
    };
    /**
     * Type => type of search (search,tags)
     * value => value of search (fotball,test)
     * this function along with removeFilterParamAndSearch does most of the search and sort work,
     * calling either will perform a new search using the updated filterParams map
     */
    const addParamAndSearch = (type, value) => {
      if (filterParams.has(type) && (type === "tags" || type === "dates")) {
        if (filterStringArray.includes(translationMap.get(type) + ": " + value))
          return;
        filterParams.set(type, filterParams.get(type) + "," + value);
      } else {
        if (type === "intensities") {
          if (
            filterStringArray.includes(translationMap.get(type) + ": " + value)
          )
            return;
          if (!filterParams.has(type)) {
            filterParams.set(
              type,
              (
                intensityArr.findIndex((element) => element === value) + 1
              ).toString()
            );
          } else {
            filterParams.set(
              type,
              filterParams.get(type) +
                "," +
                (intensityArr.findIndex((element) => element === value) + 1)
            );
          }
        } else {
          filterParams.set(type, value);
          for (var i = 0; i < filterStringArray.length; i++) {
            if (filterStringArray[i].startsWith(translationMap.get(type)))
              filterStringArray.splice(i, 1);
          }
        }
      }
      searchAxios();
      filterStringArray.push(translationMap.get(type) + ": " + value);
    };

    const removeFilterParamAndSearch = (keyString) => {
      var values = keyString.split(": ");
      for (var i = 0; i < filterStringArray.length; i++) {
        if (filterStringArray[i] === keyString) {
          filterStringArray.splice(i, 1);
        }
      }
      values[0] = new Map(
        [...translationMap.entries()].map(([key, value]) => [value, key])
      ).get(values[0]);
      if (
        (values[0] === "tags" ||
          values[0] === "dates" ||
          values[0] === "intensities") &&
        filterParams.get(values[0]).includes(",")
      ) {
        removeParamWithMultipleValues(values[0], values[1]);
      } else filterParams.delete(values[0]);
      searchAxios();
    };

    const removeParamWithMultipleValues = (type, valueString) => {
      if (type === "intensities") {
        valueString = (intensityArr.indexOf(valueString) + 1).toString();
      }
      var tempString = filterParams.get(type);
      var tempArray = tempString.split(",");
      var filtered = tempArray.filter(function (value) {
        return value !== valueString;
      });
      filterParams.set(type, filtered.toString());
    };

    // compstring is for the search and sort parameters of the request
    const searchAxios = async () => {
      activities.splice(0, activities.length);
      let compString = "";
      let firstLoop = true;
      for (let [key, value] of filterParams) {
        if (!firstLoop) {
          compString += "&";
        } else firstLoop = false;
        compString += key + "=";
        compString += value;
      }
      if (
        tempSortQuery.value.desc !== undefined &&
        tempSortQuery.value.desc !== ""
      ) {
        let sortString = "sort=" + tempSortQuery.value.desc;
        if (compString !== "") {
          sortString = "&" + sortString;
        }
        compString += sortString;
      }
      let dateString = "page=" + (currentPage.value - 1);
      if (compString !== "") dateString = "&" + dateString;
      compString += dateString;

      const response = await request(
        `${baseURL}/activities?` + compString,
        GET
      );
      nPages.value = response.data.totalPages;
      for (const a of response.data.content) {
        if (a.location === null) {
          a.location = {
            address: "adresse 1",
            longitude: 1,
            latitude: 1,
          };
        }

        if (
          a.intensity === null ||
          a.intensity === undefined ||
          a.intensity === "undefined"
        ) {
          a.intensity = 1;
        }

        if (a.description === null) {
          a.description = "";
        }

        if (a.createdBy === null) {
          a.createdBy = {};
        }

        if (a.createdBy.firstName === null) {
          a.createdBy.firstName = "empty";
        }
        if (a.createdBy.lastName === null) {
          a.createdBy.lastName = "empty";
        }

        if (a.equipment === null) {
          a.equipment = [{ item: "", amount: 1 }];
        }

        a.start = new Date(a.start);
        a.end = new Date(a.end);

        activities.push(a);
      }
    };

    const hideMessage = () => {
      msgVar.value = "";
    };

    let details = ref(false);
    let selectedActivity = reactive({});

    const activityDetails = (a) => {
      if (tagclicked.value) {
        tagclicked.value = false;
        return;
      }
      Object.assign(selectedActivity, a);
      getComments();
      details.value = true;
    };

    const activityDetailsBack = () => {
      details.value = false;
    };

    const hasSameId = (ele) => ele.id === selectedActivity.id;

    const replaceActivity = (actData) => {
      let indexAct = activities.findIndex(hasSameId);
      actData.start = new Date(actData.start);
      actData.end = new Date(actData.end);
      if (indexAct > -1) {
        activities.splice(indexAct, 1, actData);
      }
    };

    const listenOnParticipation = (el) => {
      replaceActivity(el);

      let indexJoined = joinedActivities.value.indexOf(el.id);

      if (indexJoined > -1) {
        joinedActivities.value.splice(indexJoined, 1);
      } else {
        joinedActivities.value.push(el.id);
      }
    };

    const getComments = async () => {
      const response = await request(
        `${baseURL}/activity/${selectedActivity.id}/comments`,
        GET
      );
      if (response.status === 200) {
        comments.value = response.data;
      } else {
        console.error("an error happened while retrieving comments");
      }
    };

    const submitComment = async () => {
      const obj = { message: msg.value };

      const response = await request(
        `${baseURL}/activity/${selectedActivity.id}/comments`,
        PUT,
        JSON.stringify(obj)
      );

      if (response.status === 200) {
        getComments();
      } else {
        console.error("an error happened while posting comment");
      }
    };

    onMounted(async () => {
      if (window.localStorage.getItem("firstName") !== null) {
        logged.value = true;

        const response1 = await request(
          `${baseURL}/profile/activities/upcoming`,
          GET
        );

        for (const j of response1.data) {
          joinedActivities.value.push(j.id);
        }
      }

      await getTags();
      loadBool.value = true;
      if (props.msgActCreate) msgVar.value = props.msgActCreate;
      searchAxios();
    });

    return {
      createActivity,
      details,
      activityDetails,
      activityDetailsBack,
      selectedActivity,
      listenOnSave,
      listenOnDelete,
      listenOnSearch,
      search,
      joinedActivities,
      logged,
      activities,
      removeFilterParamAndSearch,
      filterStringArray,
      comments,
      getComments,
      submitComment,
      msg,
      listenOnSort,
      loadBool,
      listenOnTagClick,
      listenOnParticipation,
      hideMessage,
      msgVar,
      currentPage,
      nPages,
      changePage,
    };
  },
};
</script>

<style scoped>
.sect {
  padding: 10px;
  width: inherit;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  flex-direction: column;
}
#topDiv {
  display: grid;
  grid-template-columns: auto;
  grid-template-rows: auto;
}
#topDiv * {
  margin: 10px;
}
.create {
  grid-column-start: 1;
  grid-column-end: 1;
  grid-row-start: 2;
  grid-row-end: 2;
}
.searchbar {
  grid-column-start: 1;
  grid-column-end: 1;
  grid-row-start: 3;
  grid-row-end: 3;
}
.title {
  grid-column-start: 1;
  grid-column-end: 1;
  grid-row-start: 1;
  grid-row-end: 1;
}
.comments {
  margin: 0.5rem;
}
#filterParams {
  display: flex;
  flex-direction: row;
}
.sortWrapper {
  display: flex;
  flex-direction: row;
}

@media (max-width: 450px) {
  .sect {
    width: 70vw;
    margin-left: 40px;
  }
  .searchbtn {
    width: 70vw;
  }
  .create {
    width: 70vw;
  }
  .searchbar {
    width: 70vw;
  }
}

.filterButton {
  padding-right: 0px;
  pointer-events: none !important;
}
.pagination-link {
  background-color: #ed1c24;
  margin-top: 15px;
}
</style>
