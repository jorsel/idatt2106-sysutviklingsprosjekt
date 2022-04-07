<template>
  <div class="box" id="box">
    <section v-if="loggedIn" id="login">
      <p>Du er allerede pålogget</p>
    </section>

    <section v-else id="login">
      <div class="notification is-info" v-if="msgStr">
        {{ msgStr }}
      </div>

      <form @submit.prevent="loginFunc">
        <div class="field">
          <Label :id="'email'">E-mail</Label>
          <div class="control">
            <Input
              :id="'email'"
              :type="'email'"
              v-model="user.email"
              :required="required"
              :placeholder="'E-mail'"
            >
              <template v-slot:icon>
                <em class="fas fa-envelope"></em>
              </template>
            </Input>
          </div>
        </div>
        <div class="field">
          <Label :id="'password'">Passord</Label>
          <div class="control">
            <Input
              :id="'password'"
              :type="'password'"
              v-model="user.password"
              :required="required"
              :placeholder="'Passord'"
            >
              <template v-slot:icon>
                <em class="fas fa-lock"></em>
              </template>
            </Input>
          </div>
        </div>
        <Button :type="'submit'">Login</Button>
      </form>
      <p class="notification is-info" v-show="isNotFilled">
        Vennligst fyll ut parametrene
      </p>
      <div class="field is-grouped">
        <div class="control">
          <Button @click="sendToRegister">Registrer deg</Button>
          <Button @click="continueNoSignIn">Fortsett uten innlogging</Button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { onMounted, ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { POST, GET, baseURL, request } from "@/utility/request.js";

import Button from "@/components/common/Button.vue";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";

export default {
  name: "Login",
  components: {
    Button,
    Input,
    Label,
  },
  props: {
    msg: {
      type: String,
      required: false,
    },
  },
  setup(props, { emit }) {
    const user = reactive({
      email: "",
      password: "",
    });

    const isNotFilled = ref(false);
    const required = ref(true);
    const msgStr = ref(props.msg);

    const router = useRouter();

    const sendToRegister = () => {
      router.push("Register");
    };

    const continueNoSignIn = () => {
      router.push("Activities");
    };

    const loggedIn = ref(false);

    onMounted(() => {
      if (window.localStorage.getItem("firstName")) {
        loggedIn.value = true;
      }
    });

    const loginFunc = async () => {
      if (user.email === "" || user.password === "") {
        isNotFilled.value = true;
        return;
      }

      const bodyFormData = new FormData();
      bodyFormData.append("username", user.email);
      bodyFormData.append("password", user.password);
      try {
        const response = await request(`${baseURL}/login`, POST, bodyFormData);

        if (response?.status === 200) {
          Object.assign(user, {
            email: "",
            password: "",
          });

          await getUserData();
          loggedIn.value = true;
          emit("update:login");
          router.push("Activities");
        } else {
          console.error("An error occured whiled sending login request!");
          msgStr.value = "En feil oppstod og kunne ikke logge på!";
        }
      } catch {
        console.error("An error occured whiled sending login request!");
        msgStr.value = "En feil oppstod og kunne ikke logge på!";
      }
    };

    const getUserData = async () => {
      const myStorage = window.localStorage;

      try {
        const response = await request(`${baseURL}/profile`, GET);
        if (response?.status === 200) {
          if (response?.data?.firstName) {
            for (const [key, value] of Object.entries(response.data)) {
              if (key === "profilePic" && value?.content) {
                myStorage.setItem(
                  key,
                  "data:image/jpeg;base64," + value.content
                );
              } else if (key === "profilePicThumbnail" && value?.content) {
                myStorage.setItem(
                  key,
                  "data:image/jpeg;base64," + value.content
                );
              } else {
                myStorage.setItem(key, value);
              }
            }
          }
        }
      } catch {
        console.error("An error occured while fetching profile details!");
        msgStr.value = "En feil oppstod og kunne ikke logge på på!";
      }
    };

    return {
      user,
      required,
      loggedIn,
      isNotFilled,
      loginFunc,
      msgStr,
      sendToRegister,
      continueNoSignIn,
    };
  },
};
</script>
<style scoped>
#box {
  flex-direction: column;
  display: flex;
  margin-left: auto;
  margin-right: auto;
}

#login {
  background-color: white;
  padding: 10px;
  width: inherit;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  justify-content: center;
  flex-direction: column;
}
</style>
