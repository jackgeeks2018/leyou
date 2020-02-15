<template>
  <v-card>
    <!-- 卡片的头部 -->
    <v-card-title class="layout row">
      <v-btn color="primary" @click="addBrand()">新增品牌</v-btn>
      <v-spacer />
      <v-text-field
        label="输入关键字搜索"
        v-model.lazy="search"
        hide-details
        append-icon="search"
        class="flex sm3"
      />
    </v-card-title>
    <!-- 分割线 -->
    <v-divider />
    <v-data-table
      :headers="headers"
      :items="brands"
      :search="search"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <img :src="props.item.image" />
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="justify-center layout px-0">
          <v-btn small icon class="mx-0" fit @click="editBrand(props.item)">
            <v-icon color="success">edit</v-icon>
          </v-btn>
          <v-btn small icon class="mx-0" fit @click="deleteBrand(props.item)">
            <v-icon color="pink">delete</v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
    <!--弹出的对话框-->
    <v-dialog max-width="500" v-model="show" persistent>
      <v-card>
        <!--对话框的标题-->
        <v-toolbar dense dark color="primary">
          <v-toolbar-title>{{isEdit ? '修改品牌' : '新增品牌'}}</v-toolbar-title>
          <v-spacer />
          <v-btn icon @click="show=false">
            <v-icon>close</v-icon>
          </v-btn>
        </v-toolbar>
        <!--对话框的内容，表单-->
        <v-card-text class="px-5">
          <my-brand-form
            @close="closeWindow"
            :isEdit="isEdit"
            :oldBrand="oldBrand"
            :reload="getDataFromServer"
          />
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-card>
</template>
<script>
import MyBrandForm from "./MyBrandForm ";
export default {
  name: "my-Brand",
  data() {
    return {
      headers: [
        { text: "id", align: "center", value: "id" },
        { text: "名称", align: "center", sortable: false, value: "name" },
        { text: "LOGO", align: "center", sortable: false, value: "image" },
        { text: "首字母", align: "center", value: "letter", sortable: true },
        { text: "操作", align: "center", value: "id", sortable: false }
      ],
      brands: [],
      pagination: {},
      totalBrands: 0,
      loading: false,
      search: [],
      show: false,
      oldBrand: {},
      isEdit: false // 判断是编辑还是新增
    };
  },
  watch: {
    pagination: {
      deep: true,
      handler() {
        this.getDataFromServer();
      }
    },
    search() {
      this.getDataFromServer();
    }
  },
  created() {
    this.getDataFromServer();
  },
  methods: {
    getDataFromServer() {
      this.loading = true;
      // 发起请求
      this.$http
        .get("/item/brand/page", {
          params: {
            key: this.search, // 搜索条件
            page: this.pagination.page, // 当前页
            rows: this.pagination.rowsPerPage, // 每页大小
            sortBy: this.pagination.sortBy, // 排序字段
            desc: this.pagination.descending // 是否降序
          }
        })
        .then(resp => {
          // 这里使用箭头函数
          this.brands = resp.data.items;
          this.totalBrands = resp.data.total;
          // 完成赋值后，把加载状态赋值为false
          this.loading = false;
        });
    },
    addBrand() {
      // 控制弹窗可见：
      this.show = true;
      // 把oldBrand变为null
      this.oldBrand = null;
    },
    closeWindow() {
      // 关闭窗口
      this.show = false;
      // 重新加载数据
      this.getDataFromServer();
    },
    editBrand(item) {
      // 查询商品分类信息，进行回显
      this.$http.get("/item/category/bid/" + item.id).then(({ data }) => {
        this.oldBrand = item;
        this.isEdit = true;
        this.show = true;
        this.oldBrand.categories = data;
      });
    },
    deleteBrand(item) {
      this.$message
        .confirm("此操作将永久删除该品牌, 是否继续?")
        .then(() => {
          // 发起删除请求
          this.$http.delete("/item/brand?id=" + item.id).then(() => {
            // 删除成功，重新加载数据
            this.$message.success("删除成功！");
            this.getDataFromServer();
          });
        })
        .catch(() => {
          this.$message.info("删除已取消！");
        });
    }
  },

  components: {
    MyBrandForm
  }
};
</script>
<style scoped>
</style>