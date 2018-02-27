<?php

use yii\helpers\Html;
use yii\widgets\DetailView;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */

$this->title = $model->nome;
$this->params['breadcrumbs'][] = ['label' => 'Publicacaousers', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaouser-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Update', ['update', 'id' => $model->nome], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Delete', ['delete', 'id' => $model->nome], [
            'class' => 'btn btn-danger',
            'data' => [
                'confirm' => 'Are you sure you want to delete this item?',
                'method' => 'post',
            ],
        ]) ?>
    </p>

    <?= DetailView::widget([
        'model' => $model,
        'attributes' => [
          //  'id',
            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',
            'email:ntext',
            'atvexercida:ntext',
            'categoria:ntext',
            'aprovado:ntext',
            'latitude',
            'longitude',
          //  'geo_gps',
            'img1:ntext',
            'img2:ntext',
            'img3:ntext',
            'img4:ntext',
          //  'campo1:ntext',
          //  'campo2:ntext',
          //  'campo3:ntext',
          //  'campo4:ntext',
          //  'campo5:ntext',
        ],
    ]) ?>

</div>
